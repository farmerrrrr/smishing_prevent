# import tokenizer as tokenizer
from .models import Message
# from tensorflow.keras.preprocessing.text import Tokenizer  # ②
# from tensorflow.keras.preprocessing.sequence import pad_sequences  # ③
# from tensorflow.keras.models import load_model  # ④
# from konlpy.tag import Mecab  # ①
from rest_framework import viewsets
from .serializer import MessageSerializer
from rest_framework import serializers
from django.http import HttpResponse, JsonResponse


# stopwords =['x', 'xx', 'xxx',  '으로', '습니다', '까지', '합니다', '에서', '입니다', '셔서', '세요', '\n']


# Create your views here.

# class MessageViewSet(viewsets.ModelViewSet):
# queryset = Message.objects.all()
# serializer_class = MessageSerializer

# def is_smishing(contents):
# mecab = Mecab()  # ①
# model = load_model('../smishing_detection_model.h5')  # ④

# new_sentence = mecab.morphs(contents)  # ① 토큰화
# new_sentence = [word for word in new_sentence if not word in stopwords]  # 불용어 제거
# encoded = tokenizer.texts_to_sequences([new_sentence])  # ② 정수 인코딩
#  pad_new = pad_sequences(encoded, maxlen=90)  # ③ 패딩
# score = float(model.predict(pad_new))  # 예측

#  return (score > 0.5)


def insert_message(request):
    id = request.GET.get('id')
    message = Message()
    message.member_id = id
    message.sender = request.GET.get('sender')
    message.contents = request.GET.get('contents')
    message.send_date = request.GET.get('send_date')
    # message.smishing_sort = is_smishing(message.contents)
    message.save()
    return HttpResponse("Success")


def load_message(request):
    if request.method == 'GET':
        m_id = request.GET.get('id')
        messages = Message.objects.filter(member_id=m_id, smishing_sort=1)
        serialized_message = MessageSerializer(messages, many=True)
        #if serialized_message is not None
            #serialized_message.save()
        return JsonResponse(serialized_message.data, status=201, safe=False, json_dumps_params={'ensure_ascii': False})
        #return JsonResponse(serialized_message.errors, status=400)
