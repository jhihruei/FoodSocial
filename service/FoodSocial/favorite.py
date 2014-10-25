#-*- coding: UTF-8 -*- 
from pymongo import MongoClient
import json
from bson.json_util import loads,dumps

def addFav(userid,groupName,postidArray):
    mc = MongoClient()
    fCon = mc.foodSocial
    fCol = fCon.favs
    fresult = fCol.update({"userID":userid,"groupName":groupName},{'$pushAll':{"groups":postidArray}},True)
    mc.close()
    return fresult

def getFav(userid):
    mc = MongoClient()
    fCon = mc.foodSocial
    fCol = fCon.favs
    fData = dumps(fCol.find({"userID":userid}))
    fData = json.loads(fData)
    mc.close()
    return fData