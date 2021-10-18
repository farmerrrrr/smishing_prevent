# Serializer 는 queryset, model 등 복잡한 데이터 set을
# JSON, XML로 쉽게 변환 가능한 Python Datatype으로 바꿔줌
# 객체의 serialization과 deserialization을 담당하는 클래스를 만든다고 생각

from rest_framework import serializers
from .models import Member


class MemberSerializer(serializers.ModelSerializer):
    class Meta:
        model = Member
        fields = ('member_name', 'member_id')
