from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

# router 생성
router = DefaultRouter()
router.register('Message', views.MessageViewSet)


urlpatterns = [
    path('', include(router.urls))
]
