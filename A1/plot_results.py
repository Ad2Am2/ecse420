import matplotlib.pyplot as plt

# Data from experimental results (4000x4000 matrices)
threads = [1, 2, 4, 8, 16]
execution_times = [345096, 182927, 178287, 92857, 78945]  # in milliseconds
speedups = [1.00, 1.88, 1.93, 3.71, 4.36]

# Convert to seconds for better readability
execution_times_sec = [t / 1000 for t in execution_times]

# Create figure with two subplots
fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5))

# Plot 1: Execution Time vs Number of Threads
ax1.plot(threads, execution_times_sec, marker='o', linewidth=2, markersize=8, color='blue')
ax1.set_xlabel('Number of Threads', fontsize=12)
ax1.set_ylabel('Execution Time (seconds)', fontsize=12)
ax1.set_title('Execution Time vs Number of Threads\n(4000x4000 Matrix)', fontsize=13, fontweight='bold')
ax1.grid(True, alpha=0.3)
ax1.set_xticks(threads)

# Plot 2: Speedup vs Number of Threads
ax2.plot(threads, speedups, marker='s', linewidth=2, markersize=8, color='green', label='Actual Speedup')
ax2.plot(threads, threads, linestyle='--', linewidth=2, color='red', label='Ideal Speedup')
ax2.set_xlabel('Number of Threads', fontsize=12)
ax2.set_ylabel('Speedup', fontsize=12)
ax2.set_title('Speedup vs Number of Threads\n(4000x4000 Matrix)', fontsize=13, fontweight='bold')
ax2.grid(True, alpha=0.3)
ax2.legend(fontsize=10)
ax2.set_xticks(threads)

plt.tight_layout()
plt.savefig('matrix_multiplication_performance.png', dpi=300, bbox_inches='tight')
print("Plot saved as 'matrix_multiplication_performance.png'")
plt.show()
