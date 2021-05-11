import pathlib
from typing import Callable
from watchdog.observers import Observer
from watchdog.events import PatternMatchingEventHandler


class FolderWatcher:
    def __init__(self, path: pathlib.Path, on_item_added: Callable[[pathlib.Path], None]):
        self._path = path
        self._pmeh = PatternMatchingEventHandler(['*'], None, False, False)
        self._pmeh.on_created = lambda x: on_item_added(pathlib.Path(x.src_path))
        self._observer = Observer()
        self._observer.schedule(self._pmeh, str(path.absolute()), False)
        self._observer.start()
