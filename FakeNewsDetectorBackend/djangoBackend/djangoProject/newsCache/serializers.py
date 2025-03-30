from rest_framework import serializers


class NewsResponseSerializer(serializers.Serializer):
    source = serializers.CharField()
    data = serializers.JSONField()
