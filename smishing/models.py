from django.db import models

# Create your models here.
#__str__을 추가하는 이유: 장고 쉘에서 객체 형태가 아닌, 내가 입력한 값 형태로 확인하기 위해서!
#import settings


class Guide(models.Model):
    member_id = models.ForeignKey('Member', on_delete=models.CASCADE)
    guide_name = models.CharField("GuideName", max_length=40)
    guide_number = models.CharField("GuideNumber", max_length=45)

    def __str__(self):
        return self.guide_name


class Member(models.Model):
    member_id = models.AutoField(primary_key=True, db_column='UserID')
    member_name = models.CharField("MemberName", max_length=10, default='')
    safe_number = models.CharField("SafeNumber", max_length=20, default='')

    def __str__(self):
        return self.member_name


class Message(models.Model):
    member_id = models.ForeignKey('Member', on_delete=models.CASCADE, default='')
    sender = models.CharField("Sender", max_length=20)
    send_date = models.DateTimeField("SendDate", auto_now_add=True)
    contents = models.CharField("Contents", max_length=150)
    smishing_sort = models.BooleanField("SmishingSort", default=False)

    def __str__(self):
        return self.sender
