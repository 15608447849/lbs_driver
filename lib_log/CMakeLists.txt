# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library. 编译生成的库的名称，注意最终生成时会在前面加`lib`
             native-lib

             # Sets the library as a shared library. 生成的库的类型，有SHARED，STATIC，MODULE
             SHARED

             # Provides a relative path to your source file(s). 要编译的源代码文件
             src/main/cpp/native-lib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
#发现需要使用到NDK中定义的Api,因为NDK中的API都已经是编译好的,而且CMake会自动去NDK目录中查找这些我们需要使用的库,所以这里只需要提供库的名称就可以
#查找要使用的库,使用变量保存它的路径
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
#为了编译时可以使用,需要将上面一步找到的库链接到我们要编译的库中
target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       #此处使用上面保存的变量
                       ${log-lib} )