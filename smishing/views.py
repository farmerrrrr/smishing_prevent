from django.http import JsonResponse
from .models import Member
from .serializer import MemberSerializer


# Create your views here.
def member_register(request):
    if request.method == 'GET':
        print("리퀘스트 로그" + str(request.body))
        name = request.GET.get('name')
        member_data = Member(member_name=name, safe_number='')
        # db에 data 저장
        member_data.save()
        # serialize 하기
        serializer = MemberSerializer(member_data)
        return JsonResponse(serializer.data, status=201, safe=False, json_dumps_params={'ensure_ascii': False})
        # return JsonResponse(serializer.errors, status=400)
