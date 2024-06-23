import base64
import hashlib
import os

from rest_framework import serializers
from rest_framework.authtoken.admin import User

from users.models import CustomUser
import users.password_helpers as PasswordHelper

class CustomUserSerializer(serializers.ModelSerializer):
    class Meta:
        """
        This is to define attributes. E.g: tables, sorting
        """
        model = CustomUser
        fields = '__all__'

    def create(self, validated_data):
        password = validated_data.get('password', None)
        password = PasswordHelper.hash_password(password)
        validated_data.update({'password': password})

        result = super().create(validated_data)
        return result

