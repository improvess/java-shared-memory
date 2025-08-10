#include <jni.h>
#include <stdio.h>
#include <iostream>

#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>

#include <unordered_map>

#include "com_improvess_shared_connector_Connector.h"

std::unordered_map<jint, jlong> shm_id_size_map;

JNIEXPORT void JNICALL Java_com_improvess_shared_connector_Connector_say_1hello(JNIEnv *env, jobject obj)
{
   printf("Hello from connector!\n");
   return;
}

JNIEXPORT void JNICALL Java_com_improvess_shared_connector_Connector_say_1hello_1again(JNIEnv *env, jobject obj, jint t)
{
   for (int i = 0; i < t; ++i)
   {
      printf("Connector say hello");
      if (i > 0)
      {
         printf(" again");
      }
      printf("!\n");
   }
   return;
}

JNIEXPORT jint JNICALL Java_com_improvess_shared_connector_Connector_get_1count(JNIEnv *, jobject)
{
   return 42;
}

JNIEXPORT jint JNICALL Java_com_improvess_shared_connector_Connector_initialize_1shared_1buffer(JNIEnv *, jobject, jint address, jlong size)
{
   jint id = shmget(address, size, 0666 | IPC_CREAT);
   shm_id_size_map[id] = size;
   return id;
}

JNIEXPORT jobject JNICALL Java_com_improvess_shared_connector_Connector_get_1shared_1buffer(JNIEnv *env, jobject, jint shm_fd)
{
   if (shm_id_size_map.find(shm_fd) != shm_id_size_map.end())
   {
      jlong size = shm_id_size_map[shm_fd];
      void *buffer = shmat(shm_fd, 0, 0);
      return env->NewDirectByteBuffer(buffer, size);
   }
   return NULL;
}

JNIEXPORT jboolean JNICALL Java_com_improvess_shared_connector_Connector_release_1shared_1buffer(JNIEnv *, jobject, jint shm_fd)
{
   return shmctl(shm_fd, IPC_RMID, NULL) > -1;
}

JNIEXPORT jint JNICALL Java_com_improvess_shared_connector_Connector_initialize_1shared_1semaphore(JNIEnv *, jobject, jint key, jint nsems, jint semflg)
{
   int flag = semflg;
   int new_flag = flag | IPC_EXCL; 
   int result = semget(key, nsems, new_flag);
   if (result > -1) {
      // we only set val on creation 
      semctl(result, 0, SETVAL, 1);
   } else if (errno == EEXIST) {
      result = semget(key, nsems, flag);
   }
   return result;
}

JNIEXPORT jboolean JNICALL Java_com_improvess_shared_connector_Connector_semaphoro_1lock(JNIEnv *, jobject, jint sem_id)
{
   if (sem_id > -1)
   {
      struct sembuf sem_op;

      sem_op.sem_num = 0;
      sem_op.sem_op = -1;
      sem_op.sem_flg = 0;
      if (semop(sem_id, &sem_op, 1) == 0) {
         return JNI_TRUE;
      }

   }
   return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_com_improvess_shared_connector_Connector_semaphoro_1unlock(JNIEnv *, jobject, jint sem_id)
{
   if (sem_id > -1)
   {
      struct sembuf sem_op;

      sem_op.sem_num = 0;
      sem_op.sem_op = 1;
      sem_op.sem_flg = 0;
      if (semop(sem_id, &sem_op, 1) == 0) {
         return JNI_TRUE;
      }

   }
   return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_com_improvess_shared_connector_Connector_release_1shared_1semaphore(JNIEnv *, jobject, jint sem_id)
{
   if (semctl(sem_id, 0, IPC_RMID) == -1) {
      return JNI_TRUE;
   }
   return JNI_FALSE;
}
