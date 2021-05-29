import matplotlib.pyplot as plt

x = [i for i in range(1, 101)]
y = [173, 155, 158, 157, 166, 166, 158, 153, 157, 156, 156, 153, 152, 164, 152, 158, 162, 151, 153, 167, 149, 156,
     160, 161, 152, 167, 153, 157, 153, 152, 150, 160, 158, 154, 153, 153, 158, 158, 155, 155, 153, 153, 159, 152,
     148, 156, 150, 156, 162, 175, 163, 152, 153, 151, 158, 154, 156, 146, 152, 156, 156, 154, 158, 152, 152, 154,
     160, 151, 167, 156, 157, 163, 155, 152, 172, 154, 153, 156, 153, 154, 156, 159, 159, 156, 152, 161, 161, 162,
     155, 155, 154, 157, 158, 167, 152, 158, 157, 159, 154, 151]

y_average = sum(y) / len(y)
print(y_average)
l = plt.plot(x, y, 'k--', label='startup time')
plt.axhline(y=y_average, color='b', linestyle='-')

plt.xlabel('round')
plt.ylabel('time')
plt.title('record of app startup')
plt.legend()
plt.show()