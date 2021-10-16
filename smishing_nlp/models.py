from django.db import models
from smishing.models import Member

# Create your models here.
class Message(models.Model):
    member_id = models.ForeignKey('smishing.Member', on_delete=models.CASCADE, default='')
    sender = models.CharField("Sender", max_length=20)
    send_date = models.DateTimeField("SendDate", auto_now_add=True)
    contents = models.CharField("Contents", max_length=150)
    smishing_sort = models.BooleanField("SmishingSort", default=False)

    def __str__(self):
        return self.sender


