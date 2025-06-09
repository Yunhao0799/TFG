from django.shortcuts import render
from rest_framework import status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated

# Create your views here.
from rest_framework.views import APIView
from rest_framework.response import Response

from .models import NewsArticle, FavoriteNews
from .services import NewsCacheService
from .serializers import NewsArticleSerializer


class NewsView(APIView):
    def get(self, request):
        query = request.query_params.get("q")
        language = request.query_params.get("lang")
        country = request.query_params.get("country")
        category = request.query_params.get("category")
        endpoint = request.query_params.get("endpoint")

        articles = NewsCacheService.get_news(query, language, country, category, endpoint)
        serializer = NewsArticleSerializer(articles, many=True, context={'request': request})
        return Response({"articles": serializer.data})


@api_view(['POST'])
@permission_classes([IsAuthenticated])
def toggle_favorite(request):
    print(request.data)
    article_id = request.data.get('article_id')
    favorite = request.data.get('favorite')
    if not article_id or favorite is None:
        return Response({'error': 'Missing article_id or toggle status'}, status=status.HTTP_400_BAD_REQUEST)

    try:
        article = NewsArticle.objects.get(id=article_id)
    except NewsArticle.DoesNotExist:
        return Response({'error': 'Article not found'}, status=status.HTTP_404_NOT_FOUND)

    is_favorite = str(favorite).lower() == "true"
    if is_favorite:
        FavoriteNews.objects.get_or_create(user=request.user, article=article)
        return Response({'status': 'added'})
    else:
        FavoriteNews.objects.filter(user=request.user, article=article).delete()
        return Response({'status': 'removed'})



@api_view(['GET'])
@permission_classes([IsAuthenticated])
def list_favorites(request):
    user = request.user
    favorite_articles = NewsArticle.objects.filter(favoritenews__user=user)
    serializer = NewsArticleSerializer(favorite_articles, many=True, context={'request': request})
    return Response({'favorites': serializer.data})
