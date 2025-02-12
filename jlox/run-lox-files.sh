javac -d out $(find jlox/src/main/java -name "*.java")
echo "Java output:"
echo "----------------"
for entry in "jlox/lox-files"/*
do
  echo "Running $entry"
  java -cp out Main $entry
  echo "----------------"
done