from django.shortcuts import render
from rest_framework import mixins
from rest_framework.authtoken.admin import User
from rest_framework.viewsets import GenericViewSet

from users.models import CustomUser
from users.serializers import CustomUserSerializer


# Create your views here.
class UserView(
    mixins.ListModelMixin,
    mixins.RetrieveModelMixin,
    mixins.UpdateModelMixin,
    mixins.DestroyModelMixin,
    mixins.CreateModelMixin,
    GenericViewSet
):
    serializer_class = CustomUserSerializer
    queryset = CustomUser.objects.all()
    # filter_backends = [SearchFilter, DjangoFilterBackend, UserFilterBackend]
    # permission_classes = [DRYPermissions]
    # filterset_class = UserFilter