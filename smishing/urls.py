# URLconf를 만들어 뷰를 URL 에 맵핑
from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

# router 생성
router = DefaultRouter()
router.register('Member', views.MemberViewSet)
router.register('Message', views.MessageViewSet)
router.register('Guide', views.GuideViewSet)

urlpatterns = [
    path('', include(router.urls))
]
