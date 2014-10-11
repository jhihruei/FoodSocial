#-*- coding: UTF-8 -*- 
import hashlib
from pymongo import MongoClient

def createApiKey(hashdata):
    apikey = hashlib.sha1(hashdata).hexdigest()
    return apikey

def checkApiKey(inputApikey): #傳入apiKey做驗證 成功回傳1 失敗回傳0
    mc = MongoClient()
    checkCon = mc.foodSocial
    checkCol = checkCon.users
    result = checkCol.find_one({"apiKey":inputApikey})
    mc.close()
    if result != None:
        return 1
    else :
        return 0