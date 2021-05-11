import argparse
import json
import os
import pathlib
import time
from typing import *
from concurrent.futures.thread import ThreadPoolExecutor

from FolderWatcher import FolderWatcher
from GANManager import GANManager
from gans.BaseGAN import BaseGAN


def parse_args() -> Tuple[argparse.ArgumentParser, argparse.Namespace]:
    parser = argparse.ArgumentParser(description="A interface for processing images using GANs.")
    parser.add_argument("-l", "--list_gans", action='store_true', help="List all GANs")
    parser.add_argument("gan_names", type=str, nargs='*', default=[],
                        help="The GANs to load and create folders for.")

    args = parser.parse_args()
    return parser, args


def handle_run(gans: Dict[str, BaseGAN]):
    in_path = pathlib.Path("in")
    in_path.mkdir(exist_ok=True)
    tmp_path = pathlib.Path("tmp")
    tmp_path.mkdir(exist_ok=True)
    out_path = pathlib.Path("out")
    out_path.mkdir(exist_ok=True)

    def on_new_file(file: pathlib.Path):
        filename = file.parts[-1]
        file_parts = filename.split(".", 1)
        json_ext = out_path.joinpath(pathlib.Path(filename.rsplit(".", 1)[0] + ".json"))
        if len(file_parts) != 2:
            err = {
                "success": "false",
                "processing_time": 0.0,
                "error": "Input filename does not contain a colon!"
            }
            json_ext.write_text(json.dumps(err))
            return

        gan_name = file_parts[0]
        if gan_name not in gans:
            err = {
                "success": "false",
                "processing_time": 0.0,
                "error": f"GAN '{gan_name}' is not loaded!"
            }
            json_ext.write_text(json.dumps(err))
            return

        gan = gans[gan_name]
        start_time = time.time_ns()
        try:
            gan.process_image(str(file), str(out_path.joinpath(filename)), tmp_path)
        except Exception as e:
            err = {
                "success": "false",
                "processing_time": (time.time_ns() - start_time) / 1e6,
                "error": f"{e}"
            }
            json_ext.write_text(json.dumps(err))
            return

        success = {
            "success": "true",
            "processing_time": (time.time_ns() - start_time) / 1e6,
            "error": f""
        }
        json_ext.write_text(json.dumps(success))

    FolderWatcher(in_path, on_new_file)


def main():
    GANManager.register_gans()
    parser, args = parse_args()

    if args.list_gans:
        print("GAN List:")
        for gan_name in GANManager.get_registered_gans():
            print(f"\t{gan_name}")
        return

    if len(args.gan_names) == 0:
        parser.print_help()
        return

    instantiators = list(map(GANManager.get_instantiator, set(args.gan_names)))

    def _int_create(inst: Callable[[], BaseGAN]) -> Tuple[str, BaseGAN]:
        r1 = inst()
        return str(r1), r1

    with ThreadPoolExecutor(max(2, os.cpu_count() - 1)) as executor:
        instantiations = list(executor.map(_int_create, instantiators))

    gans: Dict[str, BaseGAN] = dict()
    for name, gan in instantiations:
        gans[name] = gan

    handle_run(gans)
    while True:
        time.sleep(1)


if __name__ == '__main__':
    main()
