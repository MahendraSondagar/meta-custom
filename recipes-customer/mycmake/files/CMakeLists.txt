cmake_minimum_required(VERSION 3.16)
project(cmake_test)

# Add executable
add_executable(cmake_test cmake_test.c)

# Install the executable to /usr/bin
install(TARGETS cmake_test DESTINATION bin)

