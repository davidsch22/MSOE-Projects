import pathlib
from typing import final
import torch
import torchvision.io as torchio
from PIL import Image
from abc import ABC, abstractmethod

from torchvision.io.image import ImageReadMode

from TorchDeviceManager import TorchDeviceManager


class BaseGAN(ABC):
    def __init__(self, gan_type: str, name: str, author: str, desc: str):
        self._gan_type = gan_type
        self._name = name
        self._author = author
        self._desc = desc
        self._device = TorchDeviceManager.get_device()

    @final
    def process_image(self, image_path: str, output_image_path: str, tmp_dir: pathlib.Path) -> str:
        in_filename, in_ext = image_path.rsplit('.', 1)
        in_ext: str = in_ext.lower()

        resized_image_path = str(tmp_dir.joinpath(pathlib.Path(image_path).parts[-1]))

        base_image: Image.Image = Image.open(image_path)
        base_image = base_image.resize(self._required_image_size())
        base_image.save(resized_image_path)

        in_tensor = torchio.read_image(resized_image_path, mode=ImageReadMode.RGB).to(torch.float32).to(self._device)
        float_tensor = in_tensor / 256
        out_tensor = self._process_image_internal(float_tensor)
        out_tensor = out_tensor * 256
        write_img = out_tensor.to(torch.uint8).to(torch.device("cpu"))

        if in_ext == "png":
            torchio.write_png(write_img, output_image_path)
        elif in_ext == "jpg":
            torchio.write_jpeg(write_img, output_image_path)
        else:
            raise ValueError(f"Unknown image format '{in_ext}'!")

        return output_image_path

    @abstractmethod
    def _process_image_internal(self, image_tensor: torch.Tensor) -> torch.Tensor:
        """
        Processes the input image, creating the desired output image.

        :param image_tensor: The torch tensor containing the image to process.
        :return: The torch tensor containing the output image,
        """
        pass

    @abstractmethod
    def _required_image_size(self) -> (int, int):

        """
        Return the required size of the input image for this GAN.

        :return: A tuple of ints representing the required size of the input
        image.
        """
        pass
