def _swap(A, i, j):
    """
    Swap the elements of a list at the 2 given indices
    """
    tmp = A[i]
    A[i] = A[j]
    A[j] = tmp

def _parent(i):
    """
    Return the index of the parent of the element at index i.
    Converted from 1-based indexing.
    """
    return int((i - 1) // 2)

def _left(i):
    """
    Returns the index of the left child of the element at index i.
    Converted from 1-based indexing.
    """
    return 2 * i + 1

def _right(i):
    """
    Returns the index of the right child of the element at index i.
    Converted from 1-based indexing.
    """
    return 2 * i + 2

def _heap_increase_key(A, i, key):
    """
    "Bubble" a key up the heap.
    """
    if A[i] != None:
        assert(key >= A[i])
    A[i] = key
    while (i > 0 and A[_parent(i)] < A[i]):
        _swap(A, i, _parent(i))
        i = _parent(i)

def _max_heapify(A, heap_size, i):
    """
    Element is in the list but not yet part of the heap. This
    adds i into the heap.
    """
    l = _left(i)
    r = _right(i)

    largest = i
    if l < heap_size and A[l] > A[largest]:
        largest = l

    if r < heap_size and A[r] > A[largest]:
        largest = r

    if largest != i:
        _swap(A, i, largest)
        _max_heapify(A, heap_size, largest)

def max_heap_insert(A, heap_size, key):
    """
    Inserts an element into the heap. This method
    should append a None value to the list to make
    room for the new key and call _heap_increase_key.
    """
    heap_size += 1
    A.append(None)
    _heap_increase_key(A, heap_size-1, key)

def heap_extract_max(A, heap_size):
    """
    Removes the maximum value from the heap and returns it.
    The list size should be reduced by 1.
    """
    assert(heap_size >= 1)
    max = A[0]
    A[0] = A[heap_size - 1]
    heap_size -= 1
    A.pop()
    _max_heapify(A, heap_size, 0)
    return max

def build_max_heap(A, heap_size):
    """
    Takes a list A of unordered elements and reorders the elements
    to construct a max binary heap.
    """
    for i in range(_parent(len(A)), -1, -1):
        _max_heapify(A, heap_size, i)

def heapsort(A):
    """
    Sorts a list of elements by converting the list into a heap
    and then extracting each element from biggest to smallest.
    Note that this is done in place.
    """
    heap_size = len(A)
    build_max_heap(A, heap_size)
    for i in range(len(A)-1, 0, -1):
        _swap(A, 0, i)
        heap_size -= 1
        _max_heapify(A, heap_size, 0)
