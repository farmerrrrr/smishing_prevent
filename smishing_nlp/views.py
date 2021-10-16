#import tokenizer as tokenizer
from rest_framework import viewsets
from .models import Message
#from tensorflow.keras.preprocessing.text import Tokenizer  # ②
#from tensorflow.keras.preprocessing.sequence import pad_sequences  # ③
#from tensorflow.keras.models import load_model  # ④
#from konlpy.tag import Mecab  # ①
from rest_framework import viewsets
from .serializer import MessageSerializer
from django.http import HttpResponse, JsonResponse

stopwords =['x', 'xx', 'xxx',  '으로', '습니다', '까지', '합니다', '에서', '입니다', '셔서', '세요', '\n']


# Create your views here.

class MessageViewSet(viewsets.ModelViewSet):
    queryset = Message.objects.all()
    serializer_class = MessageSerializer

#def is_smishing(contents):
   # mecab = Mecab()  # ①
   # model = load_model('../smishing_detection_model.h5')  # ④

   # new_sentence = mecab.morphs(contents)  # ① 토큰화
   # new_sentence = [word for word in new_sentence if not word in stopwords]  # 불용어 제거
   # encoded = tokenizer.texts_to_sequences([new_sentence])  # ② 정수 인코딩
   #  pad_new = pad_sequences(encoded, maxlen=90)  # ③ 패딩
   # score = float(model.predict(pad_new))  # 예측

  #  return (score > 0.5)


def insert_message(request):
    id = request.POST['id']
    message = Message()
    message.member_id = id
    message.sender = request.POST['sender']
    message.contents = request.POST['contents']
    message.send_date = request.POST['send_date']
    #message.smishing_sort = is_smishing(message.contents)
    message.save()

    return HttpResponse("Success")


def load_message(request):
    ID = request.POST['id']
    messages = Message.filter(id=ID, smishing_sort=1)
    serialized_message = MessageSerializer(messages, many=True)
    if MessageViewSet.is_valid():
        MessageViewSet.save()
        return JsonResponse(serialized_message, status=201)
    return JsonResponse(serialized_message.errors, status=400)
