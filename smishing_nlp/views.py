from rest_framework import viewsets
from .models import Message
from .serializer import MessageSerializer


# Create your views here.

class MessageViewSet(viewsets.ModelViewSet):
    queryset = Message.objects.all()
    serializer_class = MessageSerializer