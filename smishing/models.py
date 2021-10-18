from django.db import models


# Create your models here.
# __str__을 추가하는 이유: 장고 쉘에서 객체 형태가 아닌, 내가 입력한 값 형태로 확인하기 위해서!
# import settings

class Member(models.Model):
    member_id = models.AutoField(primary_key=True, db_column='UserID')
    member_name = models.CharField("MemberName", max_length=10, default='')
    safe_number = models.CharField("SafeNumber", max_length=20, default='')

    def __str__(self):
        return self.member_name
