import os

import torch
from transformers import RobertaTokenizer, RobertaForSequenceClassification

from ..IInferrer import IInferrer


class RobertaInferrer(IInferrer):
    def __init__(self):
        folder_path = os.path.join(os.path.dirname(__file__), "model")
        print(folder_path)
        self.tokenizer = RobertaTokenizer.from_pretrained(folder_path)
        self.model = RobertaForSequenceClassification.from_pretrained(folder_path)

    def infer(self, sentence: str):
        inputs = self.tokenizer(sentence, return_tensors="pt", padding=True, truncation=True, max_length=512)

        # Perform inference
        with torch.no_grad():
            outputs = self.model(**inputs)

        # Get predictions (softmax probabilities and class index)
        probabilities = torch.softmax(outputs.logits, dim=-1)
        predicted_class = probabilities.argmax(dim=-1).item()
        real_confidence = probabilities[0][0].item()
        fake_confidence = probabilities[0][1].item()

        return predicted_class, real_confidence, fake_confidence
