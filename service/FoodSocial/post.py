#-*- coding: UTF-8 -*- 
from pymongo import MongoClient
from datetime import datetime
import json
from bson.json_util import loads,dumps
from flask import jsonify

def post_to_mongo(poster,recommendBy,title,address,latitude,content,picture):
    mc = MongoClient()
    postCon = mc.foodSocial
    postCol = postCon.posts
    postData = {"postID":getPostNextSeq(),"poster":int(poster),"recommendBy":int(recommendBy),"title":title,"address":address,"latitude":latitude,"content":content,"picture":picture,"postTime":datetime.now()}
    postCheck = postCol.insert(postData)
    mc.close()
    return postCheck


def getPostNextSeq(): #回傳下一個posts collection的postID index
    pmc = MongoClient()
    seqCon = pmc.foodSocial.posts
    seqjson = seqCon.find_and_modify(query={"_id":"postID"}, update={'$inc': {'seq':1}},upsert=False, full_response=True)
    pmc.close()
    return seqjson['value']['seq'] + 1

def getUserPostWall(userid):
    mc = MongoClient() 
    wallCon = mc.foodSocial
    userWallCol = wallCon.users
    userWall = userWallCol.find_one({"userID":int(userid)},{"followUser":1})
    wallCol = wallCon.posts
    wallData = dumps(wallCol.find({"poster":{"$in":userWall['followUser']}}).sort("postTime",1))
    wallData = json.loads(wallData)
    return wallData
