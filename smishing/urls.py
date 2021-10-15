# URLconf를 만들어 뷰를 URL 에 맵핑
from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views


urlpatterns = [
    path('register/', views.member_register),
]
