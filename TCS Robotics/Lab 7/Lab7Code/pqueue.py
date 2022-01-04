import heapq
from typing import List, Tuple


class PriorityQueue:
    def __init__(self):
        self.elements: List[Tuple[float, List[Tuple[int, int]]]] = []

    def empty(self) -> bool:
        return not self.elements

    def put(self, item: List[Tuple[int, int]], priority: float):
        heapq.heappush(self.elements, (priority, item))

    def put_many(self, items: List[Tuple[List[Tuple[int, int]], float]]):
        for item in items:
            self.put(item[0], item[1])

    def get(self) -> List[Tuple[int, int]]:
        return heapq.heappop(self.elements)[1]
