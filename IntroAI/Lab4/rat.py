#
# CS2400 Introduction to AI
# rat.py
#
# Spring, 2020
#
# Author: Alexandre Clavel and David Schulz
#
# Stub class for Lab 2 
# This class creates a Rat agent to be used to explore a Dungeon
# 
# Note: Instance variables with a single preceding underscore are intended
# to be protected, so setters and getters are included to enable this convention.
#
# Note: The -> notation in the function definition line is a type hint.  This 
# will make identifying the appropriate return type easier, but they are not 
# enforced by Python.  
#

from queue import Queue
from queue import PriorityQueue
from dungeon import Dungeon, Room
from typing import *
from dataclasses import dataclass, field

@dataclass(order=True)
class RoomInfo:

    def __init__(self, actual_cost: int, estimated_cost: int, room: Any = field(compare=False)):
        self._room = room
        self._total_cost = estimated_cost + actual_cost

    @property
    def room(self) -> Room:
        return self._room

    @property
    def total_cost(self) -> int:
        return self._room

class Rat:

    """Represents a Rat agent in a dungeon. It enables navigation of the 
    dungeon space through searching.

    Attributes:
        dungeon (Dungeon): identifier for the dungeon to be explored
        start_location (Room): identifier for current location of the rat
    """
    def __init__(self, dungeon: Dungeon, start_location: Room):
        """ This constructor stores the references when the Rat is 
        initialized. """
        self._currentDungeon = dungeon
        self._startLocation = start_location
        self.debug = False
        self.lastSearchRooms = []

    @property
    def dungeon(self) -> Dungeon:
        """ This function returns a reference to the dungeon.  """
        return self._currentDungeon

    def set_echo_rooms_searched(self) -> None:
        """ The _self_rooms_searched variable is used as a flag for whether
        the rat should display rooms as they are visited. """
        self.debug = True

    def path_to(self, target_location: Room) -> List[Room]:
        """ This function finds and returns a list of rooms from 
        start_location to target_location.  The list will include
        both the start and destination, and if there isn't a path
        the list will be empty. This function uses depth first search. """
        self.lastSearchRooms.clear()
        path = self._dfs_search(self._startLocation, target_location, {})
        if path is not None:
            return path
        return []

    def directions_to(self, target_location: Room) -> List[str]:
        """ This function returns a list of the names of the rooms from the
        start_location to the target_location. """
        rooms = self.path_to(target_location)
        names = []
        for r in rooms:
            names.append(r.name)
        return names

    def _dfs_search(self, current_location: Room, target_location: Room, visited: dict) -> Optional[List[Room]]:
        self.lastSearchRooms.append(current_location)
        if self.debug:
            print("Visiting:", current_location.name)
        if current_location is target_location:
            return [current_location]
        visited[current_location] = True
        for neighbor in current_location.neighbors():
            if neighbor not in visited or not visited[neighbor]:
                child_result = self._dfs_search(neighbor, target_location, visited)
                if child_result is not None:
                    child_result.insert(0, current_location)
                    return child_result
        return None

    def bfs_directions_to(self, target_location: Room) -> List[str]:
        """Return the list of rooms names from the rat's current location to
        the target location. Uses breadth-first search."""
        rooms = self.bfs_path_to(target_location)
        names = []
        for r in rooms:
            names.append(r.name)
        return names
        pass

    def bfs_path_to(self, target_location: Room) -> List[Room]:
        """Returns the list of rooms from the start location to the
        target location, using breadth-first search to find the path."""
        self.lastSearchRooms.clear()
        path = self._bfs_search(self._startLocation, target_location)
        if path is not None:
            return path
        return []

    def _bfs_search(self, initial_location: Room, target_location: Room) -> Optional[list]:
        visited = {}
        to_visit = Queue()
        to_visit.put(initial_location)
        visited[initial_location] = [initial_location]
        while not to_visit.empty():
            current_location = to_visit.get()
            self.lastSearchRooms.append(current_location)
            if self.debug:
                print("Visiting:", current_location.name)
            if current_location is target_location:
                path_to = [current_location]
                while current_location is not initial_location:
                    current_location = visited[current_location]
                    path_to.insert(0, current_location)
                return path_to
            for neighbor in current_location.neighbors():
                if neighbor not in visited:
                    to_visit.put(neighbor)
                    visited[neighbor] = current_location
        return None

    def _dfs_search_depth_limited(self, current_location: Room,target_location: Room, depth: int,
                                  max_depth: int) -> Optional[List[Room]]:
        self.lastSearchRooms.append(current_location)
        if self.debug:
            print("Visiting:", current_location.name)
        if current_location is target_location:
            return [current_location]
        if depth < max_depth:
            for neighbor in current_location.neighbors():
                child_result = self._dfs_search_depth_limited(neighbor, target_location, depth+1, max_depth)
                if child_result is not None:
                    child_result.insert(0, current_location)
                    return child_result
        return None

    def id_directions_to(self, target_location: Room) -> List[str]:
        """Return the list of rooms names from the rat's current location to
        the target location. Uses iterative deepening."""
        rooms = self.id_path_to(target_location)
        names = []
        if rooms is not None:
            for room in rooms:
                names.append(room.name)
        return names

    def id_path_to(self, target_location: Room) -> Optional[List[Room]]:
        """Returns the list of rooms from the start location to the
        target location, using iterative deepening."""
        self.lastSearchRooms.clear()
        for i in range(1, self.dungeon.size()+1):
            potential_path = self._dfs_search_depth_limited(self._startLocation, target_location, 0, i)
            if potential_path is not None:
                return potential_path
        return None

    def astar_directions_to(self, target_location: Room) -> List[str]:
        """Return the list of rooms names from the rat's current location to
        the target location. Uses A* search.
        Implemented By: Alexandre Clavel"""
        rooms = self.astar_path_to(target_location)
        names = []
        if rooms is not None:
            for room in rooms:
                names.append(room.name)
        return names

    def astar_path_to(self, target_location: Room) -> Optional[List[Room]]:
        """Returns the list of rooms from the start location to the
        target location, using A* search to find the path.
        Implemented By: David Schulz"""
        can_rooms = PriorityQueue()
        estimated_cost = self._startLocation.estimated_cost_to(target_location)
        start_room_info = RoomInfo(0, estimated_cost, self._startLocation)
        can_rooms.put(start_room_info)
        visited = {}
        prev = {}

        while not can_rooms.empty():
            room_info = can_rooms.get()
            next_room = room_info.room
            visited[next_room.name] = True
            self.lastSearchRooms.append(next_room)

            if next_room.name == target_location.name:
                return self.__form_path(target_location, prev)
            else:
                neighbors = list(reversed(next_room.neighbors()))
                for room in neighbors:
                    if room.name not in visited:
                        if room not in prev:
                            prev[room] = next_room
                        actual_cost = self.__calculate_actual(prev, room)
                        estimated_cost = room.estimated_cost_to(target_location)
                        room_info = RoomInfo(actual_cost, estimated_cost, room)
                        can_rooms.put(room_info)
        return None

    def rooms_visited_by_last_search(self) -> List[str]:
        """Return the current list of rooms visited (in any order)
        This is used by Esubmit to validate the current rooms visited.
        Implemented By: Alexandre Clavel"""
        names = []
        for room in self.lastSearchRooms:
            names.append(room.name)
        return names

    def __calculate_actual(self, prev, current_room):
        """Implemented By: Alexandre Clavel"""
        cost = 0
        while not current_room == self._startLocation:
            current_room = prev[current_room]
            cost += 1
        return cost

    def __form_path(self, target_location: Room, prev: Dict[Room, Room]) -> List[Room]:
        """
        Use prev to reconstruct the list to get from the start room to the goal room

        :param target_location: Goal room
        :param prev: dictionary of room names mapped to their previous room names
        :return: Path to the goal room as List[Room]
        Implemented By: David Schulz
        """
        rooms = [self._startLocation]
        current_room = target_location
        while current_room is not self._startLocation:
            rooms.insert(1, current_room)
            current_room = prev[current_room]
        return rooms
