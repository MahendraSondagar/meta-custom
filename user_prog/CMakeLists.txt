
cmake_minimum_required(VERSION 3.16)
project(cmake_test)
add_executable(userprog userprog.c)
install (TARGETS userprog DESTINATION bin)
