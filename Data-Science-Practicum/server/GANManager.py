from gans.BaseGAN import BaseGAN
from typing import *

from gans.CycleGAN import CycleGAN


class GANManager:
    _registered_gans: Dict[str, Callable[[], BaseGAN]] = dict()

    @staticmethod
    def _internal_register(klazz: Type[BaseGAN], name: str, create: Callable[[str], BaseGAN]):
        name_str = klazz.__name__ + "-" + name
        GANManager._registered_gans[name_str] = lambda: create(name)

    @staticmethod
    def register_gans():
        GANManager._internal_register(CycleGAN, "style_monet_pretrained", lambda x: CycleGAN(x))


    @staticmethod
    def get_registered_gans() -> List[str]:
        return list(GANManager._registered_gans.keys())


    @staticmethod
    def get_instantiator(gan_name: str) -> Callable[[], BaseGAN]:
        ret = GANManager._registered_gans[gan_name]
        if ret is None:
            raise ValueError(f"The GAN name '{gan_name}' does not exist!")
        return ret
