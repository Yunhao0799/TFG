from rest_framework import serializers
from rest_framework.authtoken.models import Token

from newsCache.models import FavoriteNews, NewsArticle


class NewsArticleSerializer(serializers.ModelSerializer):
    source = serializers.SerializerMethodField()
    is_favorite = serializers.SerializerMethodField()

    class Meta:
        model = NewsArticle
        fields = ['id', 'title', 'description', 'url', 'urlToImage', 'published_at', 'source', 'prediction', 'is_favorite']

    def get_source(self, obj):
        return {'name': obj.source_name}

    def get_is_favorite(self, obj):
        request = self.context.get('request')
        auth_header = request.headers.get('Authorization', '').strip()

        if not auth_header or not auth_header.startswith("Token "):
            return False

        token = auth_header.split(" ")[1]
        user_id = Token.objects.get(key=token).user_id

        if not user_id:
            return False

        return FavoriteNews.objects.filter(user_id=user_id, article_id=obj).exists()
