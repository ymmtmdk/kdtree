srcs="PointSET.java KdTree.java"
cd src/main/java
cp $srcs ../../../
cd ../../../
zip submit.zip $srcs
rm $srcs
