cmake_minimum_required(VERSION 3.4.1)

add_library(encryptor SHARED encryptor/sha512.cpp encryptor/sha512-compress.cpp)