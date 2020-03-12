#!/usr/bin/env bash

set_jvm_heap_size() {
    # Check for container memory limits/request and use it to set JVM Heap size.
    # Defaults to 50% of the limit/request value.
    if [ -n "$MY_MEM_LIMIT" ]; then
        export JVM_HEAP_SIZE="$MY_MEM_LIMIT"
    elif [ -n "$MY_MEM_REQUEST" ]; then
        export JVM_HEAP_SIZE="$MY_MEM_REQUEST"
    fi

    if [ -z "$JVM_HEAP_SIZE" ]; then
        echo "Unable to detect reasonable JVM heap size, not configuring JVM heap size"
    else
        echo "Setting JVM_HEAP_SIZE to ${JVM_HEAP_SIZE}M"
    fi
}

set_set_java_opts() {
    if [ -z "$JAVA_OPTS" ]; then
        echo "\$JAVA_OPTS not set, setting JVM max heap size"
        set_jvm_heap_size
        export JAVA_OPTS="$JAVA_OPTS -Xmx${JVM_HEAP_SIZE}m"
    elif [[ ! "$JAVA_OPTS" =~ "-Xmx" ]]; then
        echo "\$JAVA_OPTS set, but no JVM max heap size flag set, setting JVM max heap size"
        set_jvm_heap_size
        export JAVA_OPTS="$JAVA_OPTS -Xmx${JVM_HEAP_SIZE}m"
    else
        echo "Not setting JVM max heap size, already set"
    fi
}
