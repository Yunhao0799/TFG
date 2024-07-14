from rest_framework import mixins
from rest_framework import mixins
from rest_framework import status
from rest_framework.authentication import TokenAuthentication
from rest_framework.authtoken.models import Token
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.viewsets import GenericViewSet

from django.contrib.auth import authenticate, login, logout


from users.serializers import CustomUserSerializer
from .models import CustomUser


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
            # login(request, user)
            # return Response({'status': 'success', 'message': 'Logged in successfully'}, status=status.HTTP_200_OK)
            token, created = Token.objects.get_or_create(user=user)
            return Response({'status': 'success', 'token': token.key})
        else:
            return Response({'status': 'error', 'message': 'Invalid credentials'}, status=status.HTTP_401_UNAUTHORIZED)

# class LogoutView(APIView):
#     def post(self, request, *args, **kwargs):
#         logout(request)
#         return Response({'message': 'Logged out successfully'}, status=status.HTTP_200_OK)

# Token-based logout example
class LogoutView(APIView):
    authentication_classes = [TokenAuthentication]
    permission_classes = [IsAuthenticated]

    def post(self, request, *args, **kwargs):
        try:
            token = Token.objects.get(user=request.user)
            token.delete()
            return Response({'message': 'Logged out successfully'}, status=status.HTTP_200_OK)
        except Token.DoesNotExist:
            return Response({'error': 'Invalid token'}, status=status.HTTP_400_BAD_REQUEST)

class PredictionView(APIView):
    def post(self, request, *args, **kwargs):
        input_string = request.data.get('input_string')

        # Validate input data
        if not input_string or not isinstance(input_string, str):
            return Response({'error': 'input_string parameter is required and must be a string'},
                            status=status.HTTP_400_BAD_REQUEST)

        # Call the prediction function
        prediction = 1.0*100

        # Return the prediction result
        return Response({'prediction': prediction}, status=status.HTTP_200_OK)