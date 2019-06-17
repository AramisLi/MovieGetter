cmake_minimum_required(VERSION 3.4.1)

add_library( ara_file_secret
             SHARED
             src/main/cpp/nihaoya.c)

find_library(
        log-lib
        log )

target_link_libraries(
                      ara_file_secret
                      ${log-lib} )

