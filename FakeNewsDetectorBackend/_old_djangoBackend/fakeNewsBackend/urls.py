from django.urls import path
from . import views

urlpatterns = [
    path("index/", views.index, name="index"),
    path("getCsrf/", views.getCsrf),
    path("createUser/", views.createUser),
    path("updateUser/", views.updateUser),
    path("deleteUser/", views.deleteUser)
]