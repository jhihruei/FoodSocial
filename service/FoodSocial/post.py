#-*- coding: UTF-8 -*- 
from pymongo import MongoClient
from datetime import datetime

def post_to_mongo(poster,recommendBy,title,address,latitude,content,picture):
    mc = MongoClient()
    postCon = mc.foodSocial
    postCol = postCon.posts
    postData = {"postID":getPostNextSeq(),"poster":poster,"recommendBy":recommendBy,"title":title,"address":address,"latitude":latitude,"content":content,"picture":picture,"postTime":datetime.now()}
    postCheck = postCol.insert(postData)
    mc.close()
    return postCheck


def getPostNextSeq(): #回傳下一個posts collection的postID index
    pmc = MongoClient()
    seqCon = pmc.foodSocial.posts
    seqjson = seqCon.find_and_modify(query={"_id":"postID"}, update={'$inc': {'seq':1}},upsert=False, full_response=True)
    pmc.close()
    return seqjson['value']['seq'] + 1