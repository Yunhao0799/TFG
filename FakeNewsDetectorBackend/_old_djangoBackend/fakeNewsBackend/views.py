from django.shortcuts import render, get_object_or_404, redirect

# Create your views here.
from django.http import HttpResponse
from django.contrib.auth import login, authenticate, logout, update_session_auth_hash
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm, UserChangeForm
from django.shortcuts import render, redirect
from django.middleware.csrf import get_token
from django.http import JsonResponse


class UserView(    mixins.ListModelMixin,
    mixins.RetrieveModelMixin,    mixins.UpdateModelMixin,
    GenericViewSet):
    serializer_class = UserSerializer    queryset = User.objects.all()
    filter_backends = [SearchFilter, DjangoFilterBackend, UserFilterBackend]    permission_classes = [DRYPermissions]
    filterset_class = UserFilter





























# def index(request):
#     return HttpResponse("Hello world. You are at the index")

# def createUser(request):
#     newUser = User.objects.create_user(username="test2",
#                                        password="test2")

#     isValid = newUser.check_password("test2")
#     if isValid:
#         return HttpResponse("User created and good passwrd")
#     else:
#         return HttpResponse("Bad password")

# def getCsrf(request):
#     get_token(request)

#     return JsonResponse({
#                             'success' : True,
#                             'message' : 'Token set'
#                         })

# def createUser(request):
#     if request.method == 'POST':
#         form = UserCreationForm(request.POST)
#         if form.is_valid():
#             form.save()
#             username = form.cleaned_data.get('username')
#             raw_password = form.cleaned_data.get('password1')
#             user = authenticate(username=username, password=raw_password)
#             return JsonResponse({
#                                     'success' : True,
#                                     'message' : 'User has been created'
#                                 })
#     else:
#         form = UserCreationForm()
#     return render(request, 'user_management/user_form.html', {'form': form})

# def updateUser(request, pk):
#     if request.method == 'POST':
#         form = UserChangeForm(request.POST, instance=request.user)
#         if form.is_valid():
#             form.save()
#             return redirect('home')
#     else:
#         form = UserChangeForm(instance=request.user)
#     return render(request, 'users/update_user.html', {'form': form})

# def deleteUser(request, pk):
#     user = request.user
#     user.delete()
#     return redirect('signup')