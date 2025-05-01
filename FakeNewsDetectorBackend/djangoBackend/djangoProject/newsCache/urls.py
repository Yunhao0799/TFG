from django.urls import path
from .views import NewsView, toggle_favorite, list_favorites

urlpatterns = [
    path('', NewsView.as_view(), name='news-view'),
    path('favorite-toggle/', toggle_favorite, name='toggle-favorite'),
    path('favorites/', list_favorites, name='list-favorites'),
]