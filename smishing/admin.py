#admin 사이트에서 smishing app을 관리 가능하도록 만들기
#app 자체를 import가 아니라 model들을 일일히 import 해줘야하는듯
from django.contrib import admin
from .models import Member

# Register your models here.
admin.site.register(Member)
