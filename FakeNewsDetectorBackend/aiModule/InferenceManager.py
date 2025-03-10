from enum import Enum
from roberta_inferrer.inference import RobertaInferrer
from SingletonMeta import SingletonMeta


class Model(Enum):
    ROBERTA = 0
    OPEN_AI = 1


class InferenceManager(metaclass=SingletonMeta):

    def __init__(self):
        self.data = {Model.ROBERTA: RobertaInferrer()}

    def __getitem__(self, key):
        return self.data[key]
