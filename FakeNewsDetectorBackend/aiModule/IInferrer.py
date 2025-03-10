from abc import ABC, abstractmethod

class IInferrer(ABC):
    @abstractmethod
    def infer(self, sentence: str) -> tuple[int, list[int]]:
        pass