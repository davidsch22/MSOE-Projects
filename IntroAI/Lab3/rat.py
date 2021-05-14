#
# CS2400 Introduction to AI
# rat.py
#
# Spring, 2020
#
# Author: David Schulz
#
# Stub class for Lab 2 
# This class creates a Rat agent to be used to explore a Dungeon
# 
# Note: Instance variables with a single preceeding underscore are intended 
# to be protected, so setters and getters are included to enable this convention.
#
# Note: The -> notation in the function definition line is a type hint.  This 
# will make identifying the appropriate return type easier, but they are not 
# enforced by Python.  
#

from dungeon import Dungeon, Room, Direction
from typing import *

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
        self._dungeon = dungeon
        self._start_location = start_location
        self._echo_rooms_searched = False

    @property
    def dungeon(self) -> Dungeon:
        """ This function returns a reference to the dungeon.  """
        return self._dungeon

    def set_echo_rooms_searched(self) -> None:
        """ The _self_rooms_searched variable is used as a flag for whether
        the rat should display rooms as they are visited. """
        self._echo_rooms_searched = True

    def directions_to(self, target_location: Room) -> List[str]:
        """ This function returns a list of the names of the rooms from the
        start_location to the target_location. """
        path = self.path_to(target_location)
        rooms = []
        for room in path:
            rooms.append(room.name)
        return rooms

    def path_to(self, target_location: Room) -> List[Room]:
        """ This function finds and returns a list of rooms from 
        start_location to target_location.  The list will include
        both the start and destination, and if there isn't a path
        the list will be empty. This function uses depth first search. """
        can_rooms = []
        can_rooms.append(self._start_location)
        visited = {}
        prev = {}
        
        while (len(can_rooms) > 0):
            # Take the top item of the stack and add it to the visited list.
            next_room = can_rooms.pop()
            visited[next_room.name] = True
            
            if (self._echo_rooms_searched):
                print("Visiting: " + next_room.name)
            
            # If the next room is the goal room, use prev to reconstruct the path
            if (next_room.name == target_location.name):
                # Use prev to reconstruct the list to get from the start room to the goal room
                backwards = []
                current = next_room
                backwards.append(current)
                while (prev.get(current, "") != ""):
                    current = prev.get(current)
                    backwards.append(current)

                path = []
                while (len(backwards) > 0):
                    path.append(backwards.pop())
                return path
            
            # Create a list of the current room's adjacent rooms.
            # Add the ones which aren't in the visited list to the top of stack.
            else:
                in_order = next_room.neighbors()
                reverse = []
                while (len(in_order) > 0):
                    reverse.append(in_order.pop())
                
                for room in reverse:
                    if (visited.get(room.name, "") == ""):
                        prev[room] = next_room
                        can_rooms.append(room)
        return []

    def bfs_directions_to(self, target_location: Room) -> List[str]:
        """Return the list of rooms names from the rat's current location to
        the target location. Uses breadth-first search."""
        path = self.bfs_path_to(target_location)
        rooms = []
        for room in path:
            rooms.append(room.name)
        return rooms

    def bfs_path_to(self, target_location: Room) -> List[Room]:
        """Returns the list of rooms from the start location to the
        target location, using breadth-first search to find the path."""
        can_rooms = []
        can_rooms.append(self._start_location)
        visited = {}
        prev = {}

        while (len(can_rooms) > 0):
            # Take the front item of the queue and add it to the visited list.
            next_room = can_rooms.pop(0)
            visited[next_room.name] = True

            if (self._echo_rooms_searched):
                print("Visiting: " + next_room.name)

            # If the next room is the goal room, use prev to reconstruct the path
            if (next_room.name == target_location.name):
                # Use prev to reconstruct the list to get from the start room to the goal room
                backwards = []
                current = next_room
                backwards.append(current)
                while (prev.get(current, "") != ""):
                    current = prev.get(current)
                    backwards.append(current)

                path = []
                while (len(backwards) > 0):
                    path.append(backwards.pop())
                return path

            # Create a list of the current room's adjacent rooms.
            # Add the ones which aren't in the visited list to the back of queue.
            else:
                neighbors = next_room.neighbors()
                for room in neighbors:
                    if (visited.get(room.name, "") == ""):
                        if (prev.get(room, "") == ""):
                            prev[room] = next_room
                        can_rooms.append(room)
                        visited[room.name] = True
        return []

    def id_directions_to(self, target_location: Room) -> List[str]:
        """Return the list of rooms names from the rat's current location to
        the target location. Uses iterative deepening."""
        path = self.id_path_to(target_location)
        rooms = []
        for room in path:
            rooms.append(room.name)
        return rooms

    def id_path_to(self, target_location: Room) -> List[Room]:
        """Returns the list of rooms from the start location to the
        target location, using iterative deepening."""
        prev = {}

        for i in range(1, self._dungeon.size()+1):
            path = self.dfs_limit(self._start_location, target_location, i, prev)
            if (len(path) != 0):
                return path
        return []

    def dfs_limit(self, start: Room, target_location: Room, limit: int, prev: set) -> List[Room]:
        """Returns the list of rooms from the start location to the
        target location, using iterative deepening."""
        if (self._echo_rooms_searched):
            print("Visiting: " + start.name)
        
        if (start.name == target_location.name):
            # Use prev to reconstruct the list to get from the start room to the goal room
            backwards = []
            current = start
            backwards.append(current)
            while (prev.get(current, "") != ""):
                current = prev.get(current)
                backwards.append(current)

            path = []
            while (len(backwards) > 0):
                path.append(backwards.pop())
            return path

        if (limit <= 0):
            return []

        for room in start.neighbors():
            if (prev.get(room, "") == "" and room.name != self._start_location.name):
                prev[room] = start
            path = self.dfs_limit(room, target_location, limit-1, prev)
            if (len(path) != 0):
                return path
        return []