import torch
import torch.cuda


class TorchDeviceManager:
    CurrentNVGPU = 0

    @staticmethod
    def get_device() -> torch.device:
        """
        Gets the next Torch device that should be used.

        :return: The torch device that should be run on.
        """
        if torch.cuda.is_available():
            device_num = TorchDeviceManager.CurrentNVGPU
            TorchDeviceManager.CurrentNVGPU += 1
            TorchDeviceManager.CurrentNVGPU %= torch.cuda.device_count()
            return torch.device(f"cuda:{device_num}")
        else:
            return torch.device("cpu")
