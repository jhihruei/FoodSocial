#-*- coding: UTF-8 -*- 
from pymongo import MongoClient
from datetime import datetime
import json
from bson.json_util import loads,dumps
from user import getUserName

def post_to_mongo(poster,recommendBy,title,address,latitude,content,picture):
    mc = MongoClient()
    postCon = mc.foodSocial
    postCol = postCon.posts
    if poster == recommendBy:
        posterName = getUserName(poster)
        recommendByName = posterName
    else:
        posterName = getUserName(poster)
        recommendByName = getUserName(recommendBy)
    postData = {"postID":getPostNextSeq(),"poster":int(poster),"posterName":posterName,"recommendBy":int(recommendBy),"recommendByName":recommendByName,"title":title,"address":address,"latitude":latitude,"content":content,"picture":picture,"postTime":datetime.now()}
    postCheck = postCol.insert(postData)
    mc.close()
    return postCheck


def getPostNextSeq(): #回傳下一個posts collection的postID index
    pmc = MongoClient()
    seqCon = pmc.foodSocial.posts
    seqjson = seqCon.find_and_modify(query={"_id":"postID"}, update={'$inc': {'seq':1}},upsert=False, full_response=True)
    pmc.close()
    return seqjson['value']['seq'] + 1

def getUserPostWall(userid):#回傳完個人PostWall
    mc = MongoClient() 
    wallCon = mc.foodSocial
    userWallCol = wallCon.users
    userWall = userWallCol.find_one({"userID":int(userid)},{"followUser":1})#從DB取得哪些po文者需要被query
    wallCol = wallCon.posts
    wallData = dumps(wallCol.find({"poster":{"$in":userWall['followUser']}}).sort("postTime",-1))#query貼文
    wallData = json.loads(wallData)
    mc.close()
    return wallData

def getPostIDByuserID(userid,count):
    mc = MongoClient() 
    wallCon = mc.foodSocial
    userWallCol = wallCon.users
    userWall = userWallCol.find_one({"userID":int(userid)},{"followUser":1})#從DB取得哪些po文者需要被query
    wallCol = wallCon.posts
    wallData = dumps(wallCol.find({"poster":{"$in":userWall['followUser']}},{"postID":1}).sort("postTime",-1).limit(count*20))#query貼文
    wallData = json.loads(wallData)
    mc.close()
    return wallData


def getPostByID(postidArray):#根據傳入的postID array回傳內容
    mc = MongoClient()
    postCon = mc.foodSocial
    postCol = postCon.posts
    postData = dumps(postCol.find({"postID":{"$in":postidArray}}).sort("postTime",-1))
    postData = json.loads(postData)
    mc.close()
    return postData
