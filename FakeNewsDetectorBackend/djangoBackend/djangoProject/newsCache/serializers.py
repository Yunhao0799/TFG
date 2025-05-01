from rest_framework import serializers

from newsCache.models import FavoriteNews, NewsArticle


class NewsArticleSerializer(serializers.ModelSerializer):
    source = serializers.SerializerMethodField()
    is_favorite = serializers.SerializerMethodField()

    class Meta:
        model = NewsArticle
        fields = ['id', 'title', 'description', 'url', 'urlToImage', 'published_at', 'source', 'is_favorite']

    def get_source(self, obj):
        return {'name': obj.source_name}

    def get_is_favorite(self, obj):
        user = self.context.get('request').user
        if not user or not user.is_authenticated:
            return False
        return FavoriteNews.objects.filter(user=user, article=obj).exists()