from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views


urlpatterns = [
    #path('', include(router.urls)),
    path('message/', views.insert_message),
    path('load_message/', views.load_message),
    path('insert_message/', views.insert_message),
    path('is_smishing/', views.is_smishing),
]
