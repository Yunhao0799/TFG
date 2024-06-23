from django.shortcuts import render
from rest_framework import mixins, status
from rest_framework.authtoken.admin import User
from rest_framework.viewsets import GenericViewSet
from rest_framework.views import APIView
from rest_framework.response import Response
from django.contrib.auth import authenticate, login

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


class LoginView(APIView):
    def post(self, request):
        username = request.data.get('username')
        password = request.data.get('password')
        user = authenticate(request, username=username, password=password)

        if user is not None:
            login(request, user)
            return Response({'status': 'success', 'message': 'Logged in successfully'}, status=status.HTTP_200_OK)
        else:
            return Response({'status': 'error', 'message': 'Invalid credentials'}, status=status.HTTP_401_UNAUTHORIZED)
