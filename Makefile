JFLAGS = -g:none
JC = javac
JVM = java



FILE_P1 = Bani
HELPERS_P1 = 

FILE_P2 = Gard
HELPERS_P2 = 'Gard$$Interval'

FILE_P3 = Bomboane
HELPERS_P3 = 'Bomboane$$Student'

FILE_P4 = Sala
HELPERS_P4 = 'Sala$$Dumbbell' 'Sala$$MyPriorityQueue'



MAIN_FILES = $(FILE_P1) $(FILE_P2) $(FILE_P3) $(FILE_P4)
HELPER_FILES = $(HELPERS_P1) $(HELPERS_P2) $(HELPERS_P3) $(HELPERS_P4)

ALL_FILES = $(MAIN_FILES) $(HELPER_FILES)

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $(addsuffix .java,$(MAIN_FILES))

default: build

build: $(addsuffix .class,$(MAIN_FILES))

run-p1:
	$(JVM) $(FILE_P1)

run-p2:
	$(JVM) $(FILE_P2)

run-p3:
	$(JVM) $(FILE_P3)

run-p4:
	$(JVM) $(FILE_P4)

clean:
	rm -f $(addsuffix .class,$(ALL_FILES))