import matplotlib.pyplot as plt
import numpy as np

# Data from experimental results (16 threads)
matrix_sizes = [100, 200, 500, 1000, 2000, 3000, 4000]
sequential_times = [5, 29, 116, 1224, 17229, 93898, 256904]  # in milliseconds
parallel_times = [16, 6, 31, 280, 4256, 22406, 64079]  # in milliseconds

# Convert to seconds for better readability
sequential_times_sec = [t / 1000 for t in sequential_times]
parallel_times_sec = [t / 1000 for t in parallel_times]

# Create the plot
plt.figure(figsize=(10, 6))
plt.plot(matrix_sizes, sequential_times_sec, marker='o', linewidth=2, markersize=8, 
         label='Sequential', color='blue')
plt.plot(matrix_sizes, parallel_times_sec, marker='s', linewidth=2, markersize=8, 
         label='Parallel (16 threads)', color='green')

plt.xlabel('Matrix Size (N×N)', fontsize=12)
plt.ylabel('Execution Time (seconds)', fontsize=12)
plt.title('Execution Time vs Matrix Size\nSequential vs Parallel (16 threads)', 
          fontsize=13, fontweight='bold')
plt.legend(fontsize=11)
plt.grid(True, alpha=0.3)
plt.xticks(matrix_sizes)

# Use log scale for better visualization since times vary greatly
plt.yscale('log')
plt.ylabel('Execution Time (seconds, log scale)', fontsize=12)

plt.tight_layout()
plt.savefig('matrix_size_comparison.png', dpi=300, bbox_inches='tight')
print("Plot saved as 'matrix_size_comparison.png'")
plt.show()
