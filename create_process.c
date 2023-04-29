#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main() {
    printf("Root process: PID = %d\n", getpid());
    create_children(9);
    return 0;
}

void create_children(int num_children) {
    //Base case
    if (num_children == 0) {
        return;
    }

    pid_t pid_left, pid_right;

    // Create the left child
    pid_left = fork();
    if (pid_left == 0) {
        // This is the left child process
        printf("Child process: PID = %d, Parent PID = %d\n", getpid(), getppid());
        //call this function recursively
        create_children((num_children - 1) / 2);
        return;
    } else if (pid_left > 0) {
        // This is the parent process. This branch is optional.
        //printf("Parent process: PID = %d, Created left child with PID = %d\n", getpid(), pid_left);
    } else {
        // Fork failed
        perror("fork");
        return;
    }

    if (num_children > 1) {
        // Create the right child
        pid_right = fork();
        if (pid_right == 0) {
            // This is the right child process
            printf("Child process: PID = %d, Parent PID = %d\n", getpid(), getppid());
            //call this function recursively
            create_children((num_children - 2) / 2);
            return;
        } else if (pid_right > 0) {
            // This is the parent process. This branch is optional.
            //printf("Parent process: PID = %d, Created right child with PID = %d\n", getpid(), pid_right);
        } else {
            // Fork failed
            perror("fork");
            return;
        }
    }

    // Wait for both child processes to complete
    wait(NULL);
    if (num_children > 1) {
        wait(NULL);
    }
}


