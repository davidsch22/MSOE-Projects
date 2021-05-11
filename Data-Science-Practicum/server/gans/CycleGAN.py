import numpy as np
import torch

from gans.BaseGAN import BaseGAN
from models import create_model
from options.test_options import TestOptions


class CycleGAN(BaseGAN):
    def __init__(self, name: str):
        super().__init__("CycleGAN", name, "Jun-Yan Zhu, Taesung Park, Tongzhou Wang", "CycleGAN")
        opt = TestOptions()
        opt.isTrain = False
        opt.model = "test"
        opt.model_suffix = ""
        opt.name = name
        opt.checkpoints_dir = "./checkpoints"
        opt.preprocess = "scale_width"
        opt.gpu_ids = [self._device.index] if torch.cuda.is_available() else []
        opt.num_threads = 0  # test code only supports num_threads = 0
        opt.batch_size = 1  # test code only supports batch_size = 1
        opt.serial_batches = True  # disable data shuffling; comment this line if results on randomly chosen images are needed.
        opt.no_flip = True  # no flip; comment this line if results on flipped images are needed.
        opt.display_id = -1  # no visdom display; the test code saves the results to a HTML file.
        opt.input_nc = 3
        opt.output_nc = 3
        opt.ngf = 64
        opt.ndf = 64
        opt.n_layers_D = 3
        opt.netG = "resnet_9blocks"
        opt.netD = "basic"
        opt.norm = "instance"
        opt.no_dropout = True
        opt.init_type = "normal"
        opt.init_gain = 0.02
        opt.verbose = False
        opt.direction = "AtoB"
        opt.AtoB = True
        opt.preprocess = "resize_and_crop"
        opt.suffix = ""
        self._name = name
        self._model = create_model(opt)  # create a model given opt.model and other options
        self._model.setup(opt)  # regular setup: load and print networks; create schedulers

    def _process_image_internal(self, image_tensor: torch.Tensor) -> torch.Tensor:
        new_tensor = image_tensor[np.newaxis, :, :, :]
        new_tensor *= 2
        new_tensor -= 1
        self._model.set_input(new_tensor)  # unpack data from data loader
        self._model.test()  # run inference
        visuals = self._model.get_current_visuals()  # get image results
        ret = visuals['fake'][0, :, :, :]
        ret += 1
        ret /= 2
        return ret

    def _required_image_size(self) -> (int, int):
        return 256, 256

    def __str__(self):
        return f"CycleGAN-{self._name}"

    def __repr__(self):
        return self.__str__()
