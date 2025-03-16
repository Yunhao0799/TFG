from django.contrib.auth.models import AbstractUser
from django.db import models


class CustomUser(AbstractUser):
    username = models.CharField(max_length=255, unique=True, blank=True, null=True)
    email = models.EmailField(unique=True)
    password = models.CharField(max_length=255)
    salt = models.CharField(max_length=255, blank=True, null=True)
    birthdate = models.DateField(null=True, blank=True)

    def save(self, *args, **kwargs):
        if not self.username:
            self.username = self.email  # Set username = email automatically
        super().save(*args, **kwargs)
