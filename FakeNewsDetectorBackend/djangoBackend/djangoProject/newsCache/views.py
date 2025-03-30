from django.shortcuts import render

# Create your views here.
from rest_framework.views import APIView
from rest_framework.response import Response
from .services import NewsCacheService
from .serializers import NewsResponseSerializer


class NewsView(APIView):
    def get(self, request):
        query = request.query_params.get("q")
        language = request.query_params.get("lang")
        country = request.query_params.get("country")
        category = request.query_params.get("category")
        endpoint = request.query_params.get("endpoint")

        #if not query:
        #    return Response({"error": "Query parameter 'q' is required."}, status=400)

        result = NewsCacheService.get_news(query, language, country, category, endpoint)
        serializer = NewsResponseSerializer(result)
        return Response(serializer.data)
